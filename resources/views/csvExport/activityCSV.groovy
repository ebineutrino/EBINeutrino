/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

void ebiOnLoad(){
    println("on Load");
}

void ebiAfterLoad(){
    Activity.activityNameText.text = "HALLO WORLD";
    println Activity.activityNameText.text;
}