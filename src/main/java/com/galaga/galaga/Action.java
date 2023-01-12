package com.galaga.galaga;

/*
Lista akcji:
 0 - stanie w miejscu
 1 - lot na wprost
 2 - powrot do przydzielonego miejsca
 3 - skret w lewo ostry
 4 - skret w prawo ostry
 5 - skret w lewo lagodny
 6 - skret w prawo lagodny

 */




public class Action{
    int actNum;
    float time;

    public Action(int action, float duration){
        this.actNum = action;
        this.time = duration;
    }

    public int getAction(){
        return this.actNum;
    }

    public float getDuration(){
        return this.time;
    }

    public String toString(){
        String toReturn = "";
        toReturn += "action_num: "+this.actNum;
        toReturn += ", duration: "+this.time;
        return toReturn;
    }
}
