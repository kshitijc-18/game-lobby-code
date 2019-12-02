/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.demo.model;

/**
 *
 * @author kschoudh
 */
public class Lobby {
    private int id;
    private String name;
    private int entryFee;
    private int joinCount;
    private int size;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public Lobby() {
    }

    public Lobby(int id, String name, int entryFee, int joinCount, int size) {
        this.id = id;
        this.name = name;
        this.entryFee = entryFee;
        this.joinCount = joinCount;
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(int entryFee) {
        this.entryFee = entryFee;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
}
