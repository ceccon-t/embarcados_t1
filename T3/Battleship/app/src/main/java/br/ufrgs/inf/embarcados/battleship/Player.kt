package br.ufrgs.inf.embarcados.battleship

class Player(val patrol: Ship, val destroyer: Ship, val battleship: Ship, val carrier: Ship) {

    public fun getFleet(): Array<Ship> {
        return arrayOf(patrol, destroyer, battleship, carrier)
    }

    public fun hasLost(): Boolean {
        return patrol.isDead()
                && destroyer.isDead()
                && battleship.isDead()
                && carrier.isDead()
    }
}