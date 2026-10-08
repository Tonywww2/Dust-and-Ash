package com.tonywww.dustandash.platform;

//? if forge {
public class EnergyStorage extends net.minecraftforge.energy.EnergyStorage implements IEnergyStorage {
//?} else {
/*public class EnergyStorage extends net.neoforged.neoforge.energy.EnergyStorage implements IEnergyStorage {
*///?}

    public EnergyStorage(int capacity) { super(capacity); }
    public EnergyStorage(int capacity, int transfer) { super(capacity, transfer); }
    public EnergyStorage(int capacity, int receive, int extract) { super(capacity, receive, extract); }
    public EnergyStorage(int capacity, int receive, int extract, int energy) { super(capacity, receive, extract, energy); }
}
