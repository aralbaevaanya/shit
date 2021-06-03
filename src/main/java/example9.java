public class example9 {
    public enum Type {
        HYBRID,
        NEGATIVE,
        POSITIVE,
    }

    public Type getFuncType(int a, int b, int c) {
        if (a == 0) {
            if (b == 0) {
                if (c > 0) {
                    return Type.POSITIVE;
                } else {
                    return Type.NEGATIVE;
                }
            } else {
                return Type.HYBRID;
            }
        } else {
            if (a > 0) {
                if (c > b * b / (4 * a)) {
                    return Type.POSITIVE;
                } else {
                    return Type.HYBRID;
                }
            } else {
                if (c > b * b / (4 * a)) {
                    return Type.HYBRID;
                } else {
                    return Type.NEGATIVE;
                }
            }
        }
    }
}