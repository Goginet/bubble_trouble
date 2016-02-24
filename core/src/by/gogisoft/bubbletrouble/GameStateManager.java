package by.gogisoft.bubbletrouble;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.EmptyStackException;
import java.util.Stack;

import by.gogisoft.bubbletrouble.state.State;

/**
 * Created by goginet on 13.2.16.
 */
public class GameStateManager {
    private Stack<State> stack;

    public GameStateManager() {
        this.stack = new Stack();
    }

    public void push(State state){
        stack.push(state);
    }

    public void pop(){
        try {
            stack.pop().destroy();
        }
        catch (EmptyStackException e)
        {
            e.printStackTrace();
        }
    }

    public void set(State state){
        stack.pop().destroy();
        stack.push(state);
    }

    public void render(SpriteBatch batch){
        try {
            stack.peek().render(batch);
        }
        catch (EmptyStackException e)
        {

        }
    }

    public void update(float deltaTime){
        try {
            stack.peek().update(deltaTime);
        }
        catch (EmptyStackException e)
        {

        }
    }

}
