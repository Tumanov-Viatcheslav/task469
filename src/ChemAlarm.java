import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

public class ChemAlarm {

    private static void fillMatrixFromInput(int[][] data, BufferedReader input) throws IOException {
        String line;
        String[] dividedLine;
        for (int i = 0; i < data.length; i++) {
            line = input.readLine();
            dividedLine = line.split(" ");
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = Integer.parseInt(dividedLine[j]);
            }
        }
    }

    private static int[][] readDataFromFile(String file) {
        int[][] data = null;
        try(BufferedReader input = new BufferedReader(new FileReader(file))) {
            String line;
            String[] dividedLine;

            line = input.readLine();
            dividedLine = line.split(" ");
            int n = Integer.parseInt(dividedLine[0]), m = Integer.parseInt(dividedLine[1]);
            data = new int[n][m];
            fillMatrixFromInput(data, input);
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return data;
    }

    private static void writeResultToFile(int result, String fileName) {
        try(FileWriter output = new FileWriter(fileName)) {
            output.write(String.valueOf(result));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void addNeighbours(Stack<Pair<Pair<Integer>>> toCheck, int i, int j, int maxI, int maxJ) {
        if (i == maxI && j == maxJ)
            return;
        else if (i != 0 && j == maxJ) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i + 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i - 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j - 1)));
            return;
        }
        if (i == 0 && j == 0) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i + 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j + 1)));
            return;
        }
        else if (i != maxI && j == 0) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i + 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j + 1)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i - 1, j)));
            return;
        }
        if (i == 0 && j == maxJ) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i + 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j - 1)));
            return;
        }
        else if (i == 0) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i + 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j + 1)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j - 1)));
            return;
        }
        if (i == maxI && j == 0) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i - 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j + 1)));
            return;
        }
        else if (i == maxI) {
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j + 1)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i - 1, j)));
            toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j - 1)));
            return;
        }

        toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i + 1, j)));
        toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j + 1)));
        toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i - 1, j)));
        toCheck.add(new Pair<>(new Pair<>(i, j), new Pair<>(i, j - 1)));
    }

    private static int shortestPath(int[][] matrix) {
        IntWithFlag[][] fillMatrix = new IntWithFlag[matrix.length][matrix[0].length];
        int[][] debugMatrix = new int[matrix.length][matrix[0].length];
        Pair<Pair<Integer>> step;
        Stack<Pair<Pair<Integer>>> toCheck = new Stack<>();

        for (int i = 0; i < fillMatrix.length; i++) {
            for (int j = 0; j < fillMatrix[0].length; j++) {
                fillMatrix[i][j] = new IntWithFlag();
            }
        }

        addNeighbours(toCheck, 0, 0, fillMatrix.length - 1, fillMatrix[0].length - 1);
        fillMatrix[0][0].setValue(matrix[0][0]);
        while (!toCheck.isEmpty()) {
            step = toCheck.pop();
            if (!fillMatrix[step.o2.o1][step.o2.o2].valueSet) {
                fillMatrix[step.o2.o1][step.o2.o2].setValue(fillMatrix[step.o1.o1][step.o1.o2].value + matrix[step.o2.o1][step.o2.o2]);
                debugMatrix[step.o2.o1][step.o2.o2] = fillMatrix[step.o1.o1][step.o1.o2].value + matrix[step.o2.o1][step.o2.o2];
                addNeighbours(toCheck, step.o2.o1, step.o2.o2, fillMatrix.length - 1, fillMatrix[0].length - 1);
            }
            else if (fillMatrix[step.o2.o1][step.o2.o2].valueSet && fillMatrix[step.o1.o1][step.o1.o2].value < fillMatrix[step.o2.o1][step.o2.o2].value) {
                fillMatrix[step.o2.o1][step.o2.o2].setValue(fillMatrix[step.o1.o1][step.o1.o2].value + matrix[step.o2.o1][step.o2.o2]);
                debugMatrix[step.o2.o1][step.o2.o2] = fillMatrix[step.o1.o1][step.o1.o2].value + matrix[step.o2.o1][step.o2.o2];
                addNeighbours(toCheck, step.o2.o1, step.o2.o2, fillMatrix.length - 1, fillMatrix[0].length - 1);
            }
        }
        return fillMatrix[fillMatrix.length - 1][fillMatrix[0].length - 1].value;
    }

    public static void main(String[] args) {
        int[][] data = readDataFromFile("input.txt");
        writeResultToFile(shortestPath(data), "output.txt");
    }
}