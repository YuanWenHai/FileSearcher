# FileSearcher
A simple fileSearcher.

![image](http://i.makeagif.com/media/11-15-2016/UWYh3x.gif)

## How to
start FileSearcherActivity from our activity,do not forget the keyword.
```java
Intent intent = new Intent(MainActivity.this, FileSearcherActivity.class);
intent.putExtra("keyword",content);
//optional------

intent.putExtra("max",50 * 1024);//size filter
intent.putExtra("min",50 * 1024);
intent.putExtra("theme",R.style.SearchTheme);//set custom theme here


startActivityForResult(intent,REQUEST_CODE);
```
then we deal selected data in onActivityResult
```java
@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode ==FileSearcherActivity.OK && data != null){
            ArrayList<File> list = ( ArrayList<File>) data.getSerializableExtra("data");
            Toast.makeText(this,"you selected"+list.size()+"items",Toast.LENGTH_SHORT).show();
        }
    }
```
## Theme
optional,you can use custom theme.
```xml
<!--extends FileSearcherActivityTheme, override below attributes.-->
 <style name="SearchTheme" parent="FileSearcherActivityTheme">
        <item name="colorPrimaryDark">#212121</item> <!--status bar color-->
        <item name="colorPrimary">#424242</item> <!--toolbar,scroll bar color-->
        <item name="colorAccent">#e0e0e0</item> <!-- selected item color-->
    </style>
```
## Dependency

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
Step 2. Add the dependency

	dependencies {
		 compile 'com.github.YuanWenHai:FileSearcher:1.3.1'
	}
