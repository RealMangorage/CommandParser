package org.mangorage;

import org.mangorage.capabillity.TypedCapabilityToken;

public class Main {
    public static final TypedCapabilityToken<IMyCapability> MY_CAP = new TypedCapabilityToken<>();

    public static void main(String[] args) {
        Test test = new Test();
        test.register(MY_CAP, new IMyCapability() {
            @Override
            public void add() {
                System.out.println("ADD!");
            }
        });

        test.getCapability(MY_CAP).ifPresent(c -> {
            c.add();
        });
    }
}
