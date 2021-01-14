package parser;

public class Message {
    public MsgCode type;
    public String content;

    public Message(MsgCode type, String content) {
        this.type = type;
        this.content = content;
    }
    public String toString(){
        return (this.type +": "+this.content);
    }
}
