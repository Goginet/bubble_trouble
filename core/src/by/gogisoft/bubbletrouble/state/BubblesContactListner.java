package by.gogisoft.bubbletrouble.state;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import by.gogisoft.bubbletrouble.logic.Bubble;
import by.gogisoft.bubbletrouble.logic.BubbleContact;
import by.gogisoft.bubbletrouble.logic.PlayerBubble;

/**
 * Created by goginet on 14.2.16.
 */
public class BubblesContactListner implements ContactListener {

    private com.badlogic.gdx.utils.Queue<BubbleContact> contacts;
    private PlayerBubble playerBubble;

    public BubblesContactListner(PlayerBubble playerBubble) {
        contacts = new com.badlogic.gdx.utils.Queue<BubbleContact>();
        this.playerBubble = playerBubble;
    }

    @Override
    public void beginContact(Contact contact) {

        Body bodyA = contact.getFixtureA().getBody();
        Body bodyB = contact.getFixtureB().getBody();

        if(bodyA.getUserData()!=null && bodyB.getUserData()!=null) {                    // Эксперементальная штука, лучш не трогай
            if((bodyA.getUserData().getClass() == Bubble.class &&
                    bodyB.getUserData().getClass()==Bubble.class)
                    && (((Bubble)bodyA.getUserData()).isConnected() ||
                    ((Bubble)bodyB.getUserData()).isConnected())
                    || (bodyA.getUserData().getClass() == PlayerBubble.class ||
                    bodyB.getUserData().getClass()==PlayerBubble.class)) {
                contact.getFixtureA().setRestitution(0);
                contact.getFixtureB().setRestitution(0);
                bodyA.setLinearVelocity(0, 0);
                bodyB.setLinearVelocity(0, 0);
                BubbleContact newContact = new BubbleContact();
                newContact.bodyA = bodyA;
                newContact.bodyB = bodyB;
                newContact.point = contact.getWorldManifold().getPoints()[0];
                contacts.addFirst(newContact);
            }
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public com.badlogic.gdx.utils.Queue<BubbleContact> getContacts() {
        return contacts;
    }
}
