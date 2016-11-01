# FileSearcher
A simple fileSearcher.

![image](http://i.makeagif.com/media/11-01-2016/BqiRN9.gif)

## How to
start FileSearcherActivity from our activity,do not forget the keyword.
```java
Intent intent = new Intent(MainActivity.this, FileSearcherActivity.class);
intent.putExtra("keyword",content);
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
