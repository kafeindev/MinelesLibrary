package net.mineles.library.utils;

public final class ArrayUtils {
    private ArrayUtils() {
        throw new AssertionError("Cannot instantiate ArrayUtils");
    }

    public static <T> T[] copyOf(T[] original, int newLength) {
        T[] copy = (T[]) new Object[newLength];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLength));
        return copy;
    }

    public static <T> T[] copyOfRange(T[] original, int from, int to) {
        T[] copy = (T[]) new Object[to - from];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, to - from));
        return copy;
    }

    public static <T> boolean contains(T[] array, T value) {
        for (T element : array) {
            if (element.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
