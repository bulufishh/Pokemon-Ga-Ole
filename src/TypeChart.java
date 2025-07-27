public class TypeChart {
    public static double getEffectiveness(MoveType move, MoveType defender) {
        switch (move) {
            case FIRE:
                if (defender == MoveType.GRASS || defender == MoveType.ICE) return 2.0;
                if (defender == MoveType.FIRE || defender == MoveType.WATER || defender == MoveType.ROCK || defender == MoveType.DRAGON) return 0.5;
                break;

            case WATER:
                if (defender == MoveType.FIRE || defender == MoveType.ROCK || defender == MoveType.GROUND) return 2.0;
                if (defender == MoveType.WATER || defender == MoveType.GRASS || defender == MoveType.DRAGON) return 0.5;
                break;

            case GRASS:
                if (defender == MoveType.WATER || defender == MoveType.ROCK || defender == MoveType.GROUND) return 2.0;
                if (defender == MoveType.FIRE || defender == MoveType.GRASS || defender == MoveType.DRAGON || defender == MoveType.FLYING) return 0.5;
                break;

            case ELECTRIC:
                if (defender == MoveType.WATER || defender == MoveType.FLYING) return 2.0;
                if (defender == MoveType.ELECTRIC || defender == MoveType.GRASS || defender == MoveType.DRAGON || defender == MoveType.GROUND) return 0.5;
                break;

            case DRAGON:
                if (defender == MoveType.DRAGON) return 2.0;
                break;

            // Others are neutral unless expanded
            case ICE:
            case NORMAL:
            case DARK:
            case FLYING:
            case GROUND:
            case ROCK:
                break;
        }

        return 1.0;
    }
}
