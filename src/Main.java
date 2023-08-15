import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static List<String> readFileAsList(String fileName) {
        try (Stream<String> input = Files.lines(Paths.get(fileName))) {
            return input.map(String::strip) // убрать пробелы (Java 11)
                    .filter(s -> !s.isEmpty()) // отфильтровать пустые строки
                    .collect(Collectors.toList());
        } catch (IOException ioex) {
            throw new RuntimeException("Не смог прочитать файл: " + fileName, ioex);
        }
    }

    public static void main(String[] args) {
        List<String> map = readFileAsList("map.txt");
        int n = map.size();
        int[] s = new int[2];
        int[] t = new int[2];

        for (int i = 0; i < n; i++) {
            if ((map.get(i)).indexOf('S') != -1) {
                s[0] = i;
                s[1] = map.get(i).indexOf('S');
            }

            if (map.get(i).indexOf('T') != -1) {
                t[0] = i;
                t[1] = map.get(i).indexOf('T');
            }
        }

        System.out.println("( " + s[0] + ", " + s[1] + " )  ( " + t[0] + ", " + t[1] + " )");
        BFS(map, s, t);
    }

    static void BFS(List<String> map, int[] s, int[] t) {
        int n = map.size();
        int m = map.get(0).length();
        int INF = (int) Math.pow(10, 9);
        int[][] delta = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};
        int[][] arrayINFmap = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                arrayINFmap[i][j] = INF;
            }
        }
        String[][] arrayTFmap = new String[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                arrayTFmap[i][j] = "False";
            }
        }
        ArrayDeque<List<Integer>> deque = new ArrayDeque();
        arrayINFmap[s[0]][s[1]] = 0;
        arrayTFmap[s[0]][s[1]] = "True";
        deque.add(Arrays.stream(s).boxed().collect(Collectors.toList())); // Java 8

        while (!deque.isEmpty()) {
            int x = deque.peekFirst().get(0);
            int y = deque.peekFirst().get(1);
            deque.pop();
            for (int i = 0; i < 4; i++) {
                int nx = delta[i][0] + x;
                int ny = delta[i][1] + y;
                String[][] zzz = toMatrix(map);
                if (0 < nx && nx < n && 0 < ny && ny < m && !arrayTFmap[nx][ny].equals("True") && !zzz[nx][ny].equals("#")) {
                    arrayINFmap[nx][ny] = arrayINFmap[x][y] + 1;
                    arrayTFmap[nx][ny] = "True";
                    int[] sApdate = {nx, ny};
                    deque.add(Arrays.stream(sApdate).boxed().collect(Collectors.toList())); // Java 8
                }
            }
        }
        System.out.println(arrayINFmap[t[0]][t[1]]);
    }

    public static String[][] toMatrix(List<String> data) {
        return data.stream().map(s -> s.chars().mapToObj(c -> String.valueOf((char) c)).toArray(String[]::new)).toArray(String[][]::new);
    }
}