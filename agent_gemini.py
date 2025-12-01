import os
import subprocess
import sys
import time
import glob
import json
from dotenv import load_dotenv
from openai import OpenAI

load_dotenv()

# Configuration
API_KEY = os.getenv("OPENROUTER_API_KEY")
# Using Google Gemini 2.0 Flash Experimental via OpenRouter (Free)
MODEL_NAME = "x-ai/grok-4.1-fast:free" 
# Alternative: "anthropic/claude-3.5-sonnet" (Paid)

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key=API_KEY,
)

# --- TOOLS ---

def read_file(path):
    """Reads a file from the filesystem."""
    try:
        with open(path, "r") as f:
            content = f.read()
        print(f"📖 Reading: {path}")
        return content
    except Exception as e:
        return f"Error reading file {path}: {e}"

def write_file(path, content):
    """Writes content to a file."""
    try:
        # Ensure directory exists
        os.makedirs(os.path.dirname(path), exist_ok=True)
        with open(path, "w") as f:
            f.write(content)
        print(f"✍️ Writing to: {path}")
        return f"Successfully wrote to {path}"
    except Exception as e:
        return f"Error writing to file {path}: {e}"

def list_files(path="."):
    """Lists all files in the project (recursive, ignoring .git and .venv)."""
    files = []
    print(f"📂 Listing: {path}")
    for root, dirs, filenames in os.walk(path):
        # Ignore hidden directories
        dirs[:] = [d for d in dirs if not d.startswith(".") and d != "__pycache__" and d != "build"]
        
        for filename in filenames:
            if filename.startswith("."): continue
            files.append(os.path.join(root, filename))
    return "\n".join(files)

def run_shell(command):
    """Executes a shell command."""
    print(f"🤖 Executing: {command}")
    try:
        result = subprocess.run(command, shell=True, capture_output=True, text=True)
        output = result.stdout + result.stderr
        if result.returncode != 0:
             return f"Command failed with exit code {result.returncode}:\n{output}"
        return output
    except Exception as e:
        return f"Error executing command: {e}"

tools = [
    {
        "type": "function",
        "function": {
            "name": "read_file",
            "description": "Read the contents of a file",
            "parameters": {
                "type": "object",
                "properties": {
                    "path": {"type": "string", "description": "The path to the file to read"}
                },
                "required": ["path"]
            }
        }
    },
    {
        "type": "function",
        "function": {
            "name": "write_file",
            "description": "Write content to a file",
            "parameters": {
                "type": "object",
                "properties": {
                    "path": {"type": "string", "description": "The path to the file to write"},
                    "content": {"type": "string", "description": "The content to write"}
                },
                "required": ["path", "content"]
            }
        }
    },
    {
        "type": "function",
        "function": {
            "name": "list_files",
            "description": "List all files in the project",
            "parameters": {
                "type": "object",
                "properties": {
                    "path": {"type": "string", "description": "The directory to list (default .)"}
                }
            }
        }
    },
    {
        "type": "function",
        "function": {
            "name": "run_shell",
            "description": "Run a shell command",
            "parameters": {
                "type": "object",
                "properties": {
                    "command": {"type": "string", "description": "The command to run"}
                },
                "required": ["command"]
            }
        }
    }
]

available_functions = {
    "read_file": read_file,
    "write_file": write_file,
    "list_files": list_files,
    "run_shell": run_shell,
}

def main():
    print("🌙 Night Shift Agent Initializing (OpenRouter)...")
    print(f"✨ Connected to {MODEL_NAME}")

    # Check Task Queue
    if os.path.exists("tasks.txt"):
        while True:
            with open("tasks.txt", "r") as f:
                lines = f.readlines()
            
            # Find first unchecked task
            task_index = -1
            current_task = ""
            for i, line in enumerate(lines):
                if line.strip() and not line.strip().startswith("[x]"):
                    task_index = i
                    current_task = line.strip()
                    break
            
            if task_index != -1:
                print(f"\n▶️ Processing Task: {current_task}")
                
                # Load Context
                architecture_guide = ""
                if os.path.exists("ARCHITECTURE.md"):
                    architecture_guide = read_file("ARCHITECTURE.md")
                
                project_files = list_files()

                system_prompt = f"""You are the Night Shift Agent, an autonomous coding assistant.
                
                SYSTEM INSTRUCTIONS:
                {architecture_guide}
                
                PROJECT FILES:
                {project_files}
                
                INSTRUCTIONS:
                1. Analyze the task.
                2. Read necessary files to understand the code.
                3. Modify or create files using 'write_file'.
                4. Verify your work using 'run_shell' (e.g., run tests).
                5. CRITICAL: If the build or tests fail (exit code != 0), you MUST fix the code and retry. Do NOT mark the task as done until the build passes.
                6. Commit your changes using 'run_shell' (e.g., git commit).
                
                IMPORTANT:
                - Always use the provided tools.
                - Be concise in your reasoning.
                - NEVER finish if the code does not compile.
                """
                
                messages = [
                    {"role": "system", "content": system_prompt},
                    {"role": "user", "content": f"TASK: {current_task}"}
                ]
                
                try:
                    # Tool Loop
                    while True:
                        response = client.chat.completions.create(
                            model=MODEL_NAME,
                            messages=messages,
                            tools=tools,
                        )
                        
                        response_message = response.choices[0].message
                        messages.append(response_message)
                        
                        tool_calls = response_message.tool_calls
                        
                        if tool_calls:
                            for tool_call in tool_calls:
                                function_name = tool_call.function.name
                                function_to_call = available_functions[function_name]
                                function_args = json.loads(tool_call.function.arguments)
                                
                                # Execute tool
                                function_response = function_to_call(**function_args)
                                
                                messages.append(
                                    {
                                        "tool_call_id": tool_call.id,
                                        "role": "tool",
                                        "name": function_name,
                                        "content": str(function_response),
                                    }
                                )
                        else:
                            # No more tools, final response
                            print(f"\n🧠 Agent Report:\n{response_message.content}")
                            break

                    # Mark task as done if successful (assuming no exception)
                    lines[task_index] = f"[x] {lines[task_index]}"
                    with open("tasks.txt", "w") as f:
                        f.writelines(lines)
                    print(f"✅ Marked task as done: {current_task}")
                    
                    # Optional: Small delay to be polite to the API
                    time.sleep(2)
                    
                except Exception as e:
                    print(f"❌ Error: {e}")
                    break # Stop on error
            else:
                print("Task queue is empty (all tasks completed).")
                break
    else:
        print("No tasks.txt found.")

if __name__ == "__main__":
    main()
