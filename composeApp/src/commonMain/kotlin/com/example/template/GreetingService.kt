package com.example.template

class GreetingService {
    private val greetings = listOf(
        // Classic romantic greetings
        GreetingDetails("My Love", "Your Forever", "You make every day feel special."),
        GreetingDetails("Sweetheart", "Always Yours", "I'm so lucky to have you in my life."),
        GreetingDetails("Darling", "With Love", "Thinking of you today and always."),
        GreetingDetails("My Heart", "Yours Truly", "Every moment with you is a treasure."),
        GreetingDetails("Beloved", "Forever Yours", "You are the light of my life."),
        // Sweet and tender
        GreetingDetails("My Dearest", "With All My Heart", "You complete me in every way."),
        GreetingDetails("Honey", "Your One and Only", "Life is beautiful because of you."),
        GreetingDetails("My Angel", "Eternally Yours", "You are my dream come true."),
        GreetingDetails("My Sunshine", "Your Devoted", "You brighten every day."),
        GreetingDetails("My Everything", "Lovingly Yours", "I cherish every moment with you."),
        // Passionate expressions
        GreetingDetails("My Soulmate", "Deeply Yours", "Our love story is my favorite."),
        GreetingDetails("My Treasure", "Passionately Yours", "You are worth more than gold."),
        GreetingDetails("My World", "Completely Yours", "You mean everything to me."),
        GreetingDetails("My Star", "Adoringly Yours", "You light up my universe."),
        GreetingDetails("My Moon", "Tenderly Yours", "You guide me through the darkness."),
        // Playful and fun
        GreetingDetails("Cutie", "Your Biggest Fan", "You make my heart skip a beat."),
        GreetingDetails("Gorgeous", "Your Admirer", "I fall for you more each day."),
        GreetingDetails("Beautiful", "Smitten by You", "You take my breath away."),
        GreetingDetails("Handsome", "Head Over Heels", "You're my favorite hello."),
        GreetingDetails("Charming One", "Enchanted by You", "You cast a spell on my heart."),
        // Deep and meaningful
        GreetingDetails("My Partner", "Your Companion", "Together we can conquer anything."),
        GreetingDetails("My Best Friend", "Your Person", "You know me better than anyone."),
        GreetingDetails("My Confidant", "Your Safe Place", "With you I can be myself."),
        GreetingDetails("My Rock", "Your Anchor", "You keep me grounded."),
        GreetingDetails("My Home", "Your Haven", "Wherever you are is where I belong."),
        // Poetic expressions
        GreetingDetails("My Rose", "Your Gardener", "Our love blooms more each day."),
        GreetingDetails("My Ocean", "Your Shore", "My love for you is endless."),
        GreetingDetails("My Sky", "Your Horizon", "You expand my world."),
        GreetingDetails("My Song", "Your Melody", "You are the music in my heart."),
        GreetingDetails("My Poem", "Your Verse", "Every word reminds me of you."),
        // Timeless classics
        GreetingDetails("Precious One", "Faithfully Yours", "My love for you grows stronger."),
        GreetingDetails("Sweet Love", "Devotedly Yours", "You are my heart's desire."),
        GreetingDetails("Dear Heart", "Affectionately Yours", "You have my whole heart."),
        GreetingDetails("True Love", "Sincerely Yours", "Our love is meant to be."),
        GreetingDetails("My Valentine", "Yours Always", "Happy Valentine's Day, my love!"),
        // Additional heartfelt messages
        GreetingDetails("My Dream", "Your Reality", "You are better than any dream."),
        GreetingDetails("My Wish", "Your Star", "Every wish led me to you."),
        GreetingDetails("My Miracle", "Gratefully Yours", "Finding you was my greatest gift."),
        GreetingDetails("My Joy", "Happily Yours", "You fill my life with happiness."),
        GreetingDetails("My Peace", "Serenely Yours", "In your arms I find calm."),
        // More romantic variety
        GreetingDetails("My Heartbeat", "Rhythmically Yours", "My heart beats only for you."),
        GreetingDetails("My Destiny", "Fated to Be Yours", "We were written in the stars."),
        GreetingDetails("My Forever", "Now and Always", "I will love you for eternity."),
        GreetingDetails("My Inspiration", "Moved by You", "You make me want to be better."),
        GreetingDetails("My Reason", "Because of You", "You give my life meaning.")
    )

    fun fetchGreeting(): GreetingDetails {
        return greetings.random()
    }
}
