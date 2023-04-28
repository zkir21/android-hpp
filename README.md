# android-hpp
### Example of interaction between the HPP loaded from web-server and Android application.
In this code:

    myWebView.addJavascriptInterface(new JavaScriptInterface(this), "callback");
*callback* is the name used to expose the object in JavaScript.
Create method with appropriate name in class JavaScriptInterface and annotate it with @JavascriptInterface. For example, method named *process*:

    @JavascriptInterface
    public void process(String data) {
      Map<String, String> response = Arrays.asList(data.split(AMPERSAND))
            .stream()
            .map(p -> p.split(EQUALS, 2))
            .filter(e -> e.length == 2)
            .collect(Collectors.toMap(e -> e[0], e -> e[1], (existing, replacement) -> existing, LinkedHashMap::new));
    }
The following code shows how to reference object *callback* and method *process* in a javascript function on a web server page:

    function androidCallback(data) {
        if (callback && callback.process) {
            callback.process(data);
        }
    }
You chose on your own javascript function name (in this example - *androidCallback*).

So, *callback* javascript object name can be changed to your preference. The java method annotated with @JavascriptInterface can have the name you chose and can have more than one parameter. For example:

        @JavascriptInterface
        public void yet_one_method(String data, String username) {
          // your code
        }
You can have some methods annotated with @JavascriptInterface in your JavaScriptInterface class also.

[Main java class link](https://github.com/zkir21/android-hpp/blob/master/app/src/main/java/com/example/exampleapp/MainActivity.java)
