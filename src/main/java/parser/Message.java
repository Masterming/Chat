package parser;

public class Message {
    public Type type;
    public String content;

    public Message(Type type, String content) {
        this.type = type;
        this.content = content;
    }
    public String toString(){
        return (this.type +": "+this.content);
    }
}
