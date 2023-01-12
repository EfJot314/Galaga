package com.galaga.galaga;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Behavior {

    private List<Action> actions;

    private int behaviorLen = 20;
    private int index = 0;

    private Instant start;
    private Instant end;


    public Behavior(List<Action> startActions){
        this.actions = new ArrayList<>();
        //jelsi podano mi poczatkowe zachowania
        if(startActions != null){
            for(Action a : startActions){
                this.actions.add(a);
            }
            for(int i=0;i<(this.behaviorLen-startActions.size());i++){
                int action = randomInt(0, 6);
                int duration = randomInt(190, 210);
                //jesli wczesniej lecialem na miejsce to chce tam troche posiedziec
                if(this.actions.size() > 1){
                    if(this.actions.get(i-1).getAction() == 2){
                        action = 0;
                    }
                }
                //jesli lece na swoje miejsce to chce tam doleciec, wiec ustawiam czas trwania na duzy, aby na pewno dolecial
                //jk doleci to bedzie sobie tam siedzial tak jakby bylo 0
                if(action == 2){
                    duration = 10000;
                    //jezeli przed chwila stalem albo lecialem w to miejsce to nie chce zeby sie to powtarzalo, wiec ustawiam czas na krotki
                    if(this.actions.size() > 1){
                        if(this.actions.get(i-1).getAction() == 2 || this.actions.get(i-1).getAction() == 0){
                            duration = 10;
                        }
                    }
                }
                Action newAction = new Action(action, duration);
                this.actions.add(newAction);
            }
        }
        //jesli nic mi nie podano
        else{
            for(int i=0;i<this.behaviorLen;i++){
                int action = randomInt(0, 6);
                int duration = randomInt(190, 210);
                //jesli wczesniej lecialem na miejsce to chce tam troche posiedziec
                if(i >= 1){
                    if(this.actions.get(i-1).getAction() == 2){
                        action = 0;
                    }
                }
                //jesli lece na swoje miejsce to chce tam doleciec, wiec ustawiam czas trwania na duzy, aby na pewno dolecial
                //jk doleci to bedzie sobie tam siedzial tak jakby bylo 0
                if(action == 2){
                    duration = 10000;
                    //jezeli przed chwila stalem albo lecialem w to miejsce to nie chce zeby sie to powtarzalo, wiec ustawiam czas na krotki
                    if(i >= 1){
                        if(this.actions.get(i-1).getAction() == 2 || this.actions.get(i-1).getAction() == 0){
                            duration = 10;
                        }
                    }
                }
                Action newAction = new Action(action, duration);
                this.actions.add(newAction);
            }
        }
    }

    //zwraca null jesli nie ma potrzeby by robic cos nowego, lub nowa akcje ktora ma byc w najblizszym czasie wykonywana
    public Action getNext(){
        Action toReturn = null;
        this.end = Instant.now();
        //jesli to dopiero poczatek istnienia tego obiektu/statku
        if(this.start == null){
            this.start = Instant.now();
            toReturn = this.actions.get(this.index);
            this.index = (this.index+1)%this.behaviorLen;
        }
        //jesli minal wyznaczony czas
        else if(Duration.between(this.start, this.end).toMillis() >= this.actions.get(this.index).getDuration()){
            this.start = Instant.now();
            toReturn = this.actions.get(this.index);
            this.index = (this.index+1)%this.behaviorLen;
        }

        //zwracam co mam
        return toReturn;
    }

    //funkcja zwraca pseudolosowa liczbe calkowita z przedzialu <min, max>
    public int randomInt(int min, int max){
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


}
