public class IntWithFlag {
    int value = 0;
    boolean valueSet = false;

    public void setValue(int value) {
        this.value = value;
        valueSet = true;
    }
}
