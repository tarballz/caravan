# Lecture 2 - 10/3/17

## Activities (DIAGRAM)

- Apps are composed of activities
- Activities are self-contained tasks made up of one screen full of infomration
- Activities start one another and are destroyed often
- Apps can use activities belonging to another app
- An activity has a single content view it controls
- Activity launched
    - onCreate()
    - onStart()
    - onResume() -> activity running
        - onPause() (the activity is no longer visible)
            - onStop() -> the activity is finishing or being destroyed by the system
                - onDestroy() -> activity shut down

## Configuring Manifest - Declare Activity

We need to add an <activity> element as a child of <application> element

## Configuring Manifest - Declare Intent Filters

Launch an activity based not only on explicit request, but also an implicit one
- Do it by declaring an <intent-filter> attribute in the <activity> element
- Saying only certain activities can invoke us

## Threads

All activities run by default in the UI thread
- Drawback is you should never do in the UI thread anything that takes significant time or might stall:
    - Internet access
    - Long computation

If you can't handle UI events in a very short time, Android will declare your application unresponsive and offer to stop it.
- Processes will run multiple threads
- Don't want multiple threads for UI, that would cause chaos.

There are exceptions to the rule "everything runs in the UI thread". For example:
- We will learn how to run internet access in asynchronous threads
- A webview, and its javascript, has its own threads

For these special threads, we do have to worry about their lifespan, termination, etc

## Activity Lifetime

Full Lifetime: from onCreate() to onDestroy()

Visible Lifetime: from onStart() to onStop(). Visible, but might be paritally obscured. onRestart() called just before subsequent onStart()

Missed one here

## Activity Switch-Over Sequence

1) Activity A's onPause() method executes
2) Activity B's onCreate(), onStart(), and onResume() methods execute in sequence (Activiity B now has user focus)
3) Then, if Activity A is no longer visible on screen, onStop() its method executes

## View and Viewgroups (DIAGRAM)

Everything you see on screen is either a view or a view group. View group is a group of different views (ex: a combination of different check boxes)

## Informational Views

- TextView
- ImageView
- ProgressBar
- AnalogCLock
- DigitalClock
- VideoView

## Input Controls

- Buttons
- Check boxes
- Etc.

## Android Layout Approach (DIAGRAM)

In order to use these views, we need a hierarchy. You can statically define their locations. You can assign relative locations as well (ex: button under image view)
- If you split the screen into two parts and you have two layouts for each part, then you can have two layout structures

## Linear Layout

- Single-direction layout
- Horizontal or vertical
- Dividers
- Baseline alignment
- Weight
- Gravity

## List View

- List data provided by an adapter
- Use ArrayAdapter or a custom class to provide data
- Set OnItemClickListenter() to react to clicks on rows
- Be careful with threads in list view because if you scroll fast, there is a lot of info your activity should pull and it should only show relevant items
    - Depends on scrolling speed maximum

In code now
