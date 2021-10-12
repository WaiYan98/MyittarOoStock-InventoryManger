package com.example.myittaroostockinventorymanger.event;

public class Event<T> {

    private T content;
    private boolean hasBeenHandle;

    public Event(T content) {
        this.content = content;
    }

    public T getContentIfNotHandle() {

        if (hasBeenHandle) {
            return null;
        }

        hasBeenHandle = true;
        return content;
    }

    public T peekContent(){
        return content;
    }
}
