# Lecture 4 - 10/10/17

## Project Proposal 

## Passing data between activities

- 4 different approaches
    - Using static objects in activities (public or private) [NOT RECOMMENDED]
    - Singleton pattern
        - Is a design pattern; the implementation that each class has only one instance running at a time. 
        - If we are sure that there's only one object of that class, then we can pass it around without confusion. 
        - If a second class gets ahold of the same object, it'll have the same info stored
        - Use final to make sure it's not mutated over time
    - Using intents
        - We use intents from transferring from one activity to another.
        - The function that starts new activities accepts an intent as a parameter
        - ```Intent intent = new Intent(FirstActivity.this, SecondActivity.class); 
            intent.putExtra("some_key", value); 
            intent.putExtra("some_other_key", "a value"); 
            startActivity(intent); ```
        - We can also use bundles (Check slides)
    - Using Shared Preferences
        - ```Shared Preferences settings = getSharedPreferences(PREFS_NAME, 0); 
            SharedPreferences.Editor editor = settings.edit(); 
            editor.putBoolean("silentMode", mSilentMode); 
            editor.commit()```
            
        
