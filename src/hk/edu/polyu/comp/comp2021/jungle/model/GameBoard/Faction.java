package hk.edu.polyu.comp.comp2021.jungle.model.GameBoard;

/**
 * Two players
 */
public enum Faction {
    /**
     * Player X
     */
    X{
        @Override
        public Boolean isX() {
            return true;
        }


        @Override
        public int[] getTrapPositions() {
            return new int[]{52,58,60};
        }

        @Override
        public int getDenPosition() {
            return 59;
        }
    },
    /**
     * Player Y
     */
    Y{
        @Override
        public Boolean isX() {
            return false;
        }

        @Override
        public int[] getTrapPositions() {
            return new int[]{2,4,10};
        }

        @Override
        public int getDenPosition() {
            return 3;
        }
    };

    /**
     *
     * @return whether the Faction is Player X
     */
    public abstract Boolean isX();

    /**
     *
     * @return all the trap positions(tile ids) of Player X or Y
     */
    public abstract int[] getTrapPositions();

    /**
     *
     * @return the den position(tile id) of Player X or Y
     */
    public abstract int getDenPosition();

    /**
     *
     * @return the faction's opponent
     */
    public Faction getOpponent(){
        return this.isX()?Faction.Y:Faction.X;
    }
}
