package main;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.*;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.ext.JGraphXAdapter;

import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import com.mxgraph.view.mxEdgeStyle;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import java.awt.image.BufferedImage;

public class Main {
<<<<<<< HEAD
    private static volatile boolean stopRequested = false;
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static DirectedGraph graph;
    private static final Random random = new Random();
    private static ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 1) {
            logger.log(Level.WARNING, "Please provide the file path as a command line argument.");
            return;
        }

        String filePath = args[0];
=======
    public static volatile boolean stopRequested = false;
    public static final Logger logger = Logger.getLogger(Main.class.getName());
    public static DirectedGraph graph;
    public static final Random random = new Random();
    public static ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {

        String filePath = "test/test2.txt";
>>>>>>> Lab3b
        List<String> words = readAndProcessText(filePath);

        System.out.println("Total words: " + words);

        graph = buildGraph(words);

        // 提示用户选择操作
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (choice != 6) {
            System.out.println("Please select an option:");
            System.out.println("1. Show Directed Graph");
            System.out.println("2. Query Bridge Words");
            System.out.println("3. Generate New Text");
            System.out.println("4. Calculate Shortest Path");
            System.out.println("5. Random Walk");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            try {
                choice = scanner.nextInt();
<<<<<<< HEAD
                scanner.nextLine(); // Consume newline
=======
                scanner.nextLine(); // 消耗掉换行符

// 接下来可以安全地使用 scanner.nextLine() 或其他方法读取字符串或行

>>>>>>> Lab3b
            } catch (NoSuchElementException | IllegalStateException e) {
                logger.log(Level.SEVERE, "Scanner input error", e);
                break;
            }

            switch (choice) {
                case 1:
                    try {
                        showDirectedGraph(graph);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "Error generating graph image", e);
                    }
                    break;
                case 2:
                    System.out.print("Enter word1: ");
                    String word1 = scanner.nextLine().toLowerCase();
                    System.out.print("Enter word2: ");
                    String word2 = scanner.nextLine().toLowerCase();
                    String bridgeWords = queryBridgeWords(word1, word2);
                    System.out.println(bridgeWords);
                    break;
                case 3:
                    System.out.print("Enter input text: ");
                    String inputText = scanner.nextLine();
                    String newText = generateNewText(inputText);
                    System.out.println(newText);
                    break;
                case 4:
                    System.out.print("Enter word1: ");
                    String fromWord = scanner.nextLine().toLowerCase();
                    System.out.print("Enter word2: ");
                    String toWord = scanner.nextLine().toLowerCase();
                    String shortestPath = calcShortestPath(fromWord, toWord);
                    System.out.println(shortestPath);
                    break;
                case 5:

                    handleRandomWalk();

                    break;
                case 6:
                    // 关闭线程池
                    executor.shutdown();
                    try {
                        // 等待所有任务执行完成，或者超时
                        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                            // 如果超时仍有任务未完成，强制关闭
                            executor.shutdownNow();
                        }
                    } catch (InterruptedException e) {
                        // 捕获异常并处理
                        logger.log(Level.SEVERE, "Error waiting for executor shutdown", e);
                        Thread.currentThread().interrupt();

                    }
<<<<<<< HEAD
                    System.out.println("Exiting program...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
=======

>>>>>>> Lab3b
            }
        }
        scanner.close();
    }

    // static void handleRandomWalk(Scanner scanner) {
    //     Future<String> future = executor.submit(Main::randomWalk);
    //     try {
    //         while (!future.isDone()) {
    //             if (System.in.available() > 0 && scanner.nextLine().trim().equalsIgnoreCase("s")) {
    //                 future.cancel(true);
    //                 logger.log(Level.INFO, "Stop command received. Stopping random walk...");
    //                 break;
    //             }
    //         }
    //         String randomWalkResult = future.get();
    //         System.out.println(randomWalkResult);
    //     } catch (InterruptedException e) {
    //         // 重新中断当前线程
    //         Thread.currentThread().interrupt();
    //         logger.log(Level.SEVERE, "Random walk was interrupted", e);
    //     } catch (ExecutionException | IOException e) {
    //         logger.log(Level.SEVERE, "Error executing random walk", e);
    //     }
    // }
<<<<<<< HEAD
    static void handleRandomWalk() {
=======
    public static void handleRandomWalk() {
>>>>>>> Lab3b
        stopRequested = false; // 重置停止标志

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Thread inputListener = new Thread(() -> {
            try {
                while (!stopRequested) {
                    if (System.in.available() > 0) {
                        String input = reader.readLine().trim();
                        if (input.equalsIgnoreCase("s")) {
                            stopRequested = true;
                            logger.log(Level.INFO, "Stop command received. Stopping random walk...");
                        }
                    }
                    Thread.sleep(100); // 添加适当的延迟以减少 CPU 使用率
                }
            } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt(); // 恢复中断状态
            }
        });
    
        inputListener.start(); // 启动监听线程
        String randomWalkResult = randomWalk();
        System.out.println(randomWalkResult);
        inputListener.interrupt(); // 停止监听线程
        System.out.println("正在停止。");
        try {
            inputListener.join();
            Thread.sleep(2000);  // Pause for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore interrupted status
        }

    }

<<<<<<< HEAD
    static void showDirectedGraph(DirectedGraph graph) throws IOException {
=======
    public static void showDirectedGraph(DirectedGraph graph) throws IOException {
>>>>>>> Lab3b
        Graph<String, MyEdge> jGraphTGraph = new DefaultDirectedWeightedGraph<>(MyEdge.class);

        // 添加节点
        for (String word : graph.nodes.keySet()) {
            jGraphTGraph.addVertex(word);
        }

        // 添加边
        for (String word : graph.nodes.keySet()) {
            GraphNode node = graph.nodes.get(word);
            for (GraphEdge edge : node.edges) {
                MyEdge jGraphTEdge = new MyEdge(edge.weight);
                jGraphTGraph.addEdge(word, edge.target.word, jGraphTEdge);
                jGraphTGraph.setEdgeWeight(jGraphTEdge, edge.weight);
            }
        }

        // 使用JGraphX生成图形
        JGraphXAdapter<String, MyEdge> graphAdapter = new JGraphXAdapter<>(jGraphTGraph);

        // 设置默认的顶点和边样式
        mxStylesheet stylesheet = graphAdapter.getStylesheet();
        Map<String, Object> vertexStyle = stylesheet.getDefaultVertexStyle();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_PERIMETER, mxConstants.PERIMETER_ELLIPSE);

        Map<String, Object> edgeStyle = stylesheet.getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);
        edgeStyle.put(mxConstants.STYLE_ROUNDED, true);
        edgeStyle.put(mxConstants.STYLE_STROKEWIDTH, 1); // 调整边的宽度
        edgeStyle.put(mxConstants.STYLE_ARCSIZE, 50);
        edgeStyle.put(mxConstants.STYLE_FONTSIZE, 16); // 调整权重字号
        edgeStyle.put(mxConstants.STYLE_LABEL_POSITION, mxConstants.ALIGN_CENTER);
        edgeStyle.put(mxConstants.STYLE_VERTICAL_LABEL_POSITION, mxConstants.ALIGN_MIDDLE);
        edgeStyle.put(mxConstants.STYLE_STROKECOLOR, "#000000"); // 设置边颜色为黑色

        // 应用层次布局
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        // 处理多条边
        mxParallelEdgeLayout parallelLayout = new mxParallelEdgeLayout(graphAdapter);
        parallelLayout.execute(graphAdapter.getDefaultParent());

        // 创建图形组件并渲染为BufferedImage
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, java.awt.Color.WHITE, true,
                null);
        // 创建并设置JFrame
        JFrame frame = new JFrame("Directed Graph");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(graphComponent);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // 保存图形为PNG文件
        File imgFile = new File("graph.png");
        try {
            ImageIO.write(image, "PNG", imgFile);
            logger.log(Level.INFO, "Graph image saved as {0}", imgFile.getAbsolutePath());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error saving graph image", e);
        }
    }

<<<<<<< HEAD
    static String queryBridgeWords(String word1, String word2) {
        // 查询桥接词
=======
    public static String queryBridgeWords(String word1, String word2) {
//         查询桥接词
>>>>>>> Lab3b
        if (!graph.nodes.containsKey(word1) || !graph.nodes.containsKey(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }

        GraphNode node1 = graph.nodes.get(word1);
        GraphNode node2 = graph.nodes.get(word2);

        List<String> bridges = new ArrayList<>();
        for (GraphEdge edge1 : node1.edges) {
            for (GraphEdge edge2 : edge1.target.edges) {
                if (edge2.target == node2) {
                    bridges.add(edge1.target.word);
                }
            }
        }

        if (bridges.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        } else {
            return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridges) + ".";
        }
    }

<<<<<<< HEAD
    static String generateNewText(String inputText) {
=======
    public static String generateNewText(String inputText) {
>>>>>>> Lab3b
        // 根据bridge word生成新文本
        // 按照空格分割输入文本，并保留原文的大小写
        List<String> originalWords = new ArrayList<>(Arrays.asList(inputText.split("\\s+")));
        List<String> lowerCaseWords = new ArrayList<>();
        for (String word : originalWords) {
            lowerCaseWords.add(word.toLowerCase());
        }

        StringBuilder newText = new StringBuilder();

        for (int i = 0; i < lowerCaseWords.size() - 1; i++) {
            String word1 = lowerCaseWords.get(i);
            String word2 = lowerCaseWords.get(i + 1);
            newText.append(originalWords.get(i)).append(" ");

            String bridgeWords = queryBridgeWords(word1, word2);
            System.out.println(bridgeWords);
            if (bridgeWords.startsWith("The bridge words")) {
                String[] bridges = bridgeWords.split(": ")[1].split(", ");
                String selectedBridge = bridges[random.nextInt(bridges.length)];
                if (selectedBridge.endsWith(".")) {
                    selectedBridge = selectedBridge.substring(0, selectedBridge.length() - 1);
                }
                newText.append(selectedBridge).append(" ");
            }
        }
        newText.append(originalWords.get(originalWords.size() - 1));

        return newText.toString();
    }

<<<<<<< HEAD
    static String calcShortestPath(String word1, String word2) {
=======
    public static String calcShortestPath(String word1, String word2) {
>>>>>>> Lab3b
        // 计算两个单词之间的最短路径
        if (!graph.nodes.containsKey(word1) || !graph.nodes.containsKey(word2)) {
            return "One or both words are not in the graph!";
        }

        GraphNode startNode = graph.getNode(word1);
        GraphNode endNode = graph.getNode(word2);

        Map<GraphNode, Integer> distance = initDistanceMap();
        Map<GraphNode, GraphNode> predecessor = initPredecessorMap();
        distance.put(startNode, 0);

        PriorityQueue<GraphNode> queue = new PriorityQueue<>(Comparator.comparingInt(distance::get));
        queue.add(startNode);

        dijkstraAlgorithm(endNode, distance, predecessor, queue);

        List<String> shortestPath = buildShortestPath(endNode, predecessor);

        if (endNode != null && shortestPath.size() == 1 && !startNode.word.equals(endNode.word)) {
            return "No path found between " + word1 + " and " + word2 + "!";
        } else {
            int pathLength = distance.get(endNode);
            String path = formatShortestPath(shortestPath);
            return "Shortest path from " + word1 + " to " + word2 + ": " + path + " (Length: " + pathLength + ")";
        }
    }

    static String randomWalk() {

        // 检查图是否为空
        if (graph.nodes.isEmpty()) {
            return "The graph is empty!";
        }

        // 存储随机游走的结果
        StringBuilder walkResult = new StringBuilder();

        // 存储已经经过的边
        Set<GraphEdge> visitedEdges = new HashSet<>();

        // 随机选择一个起始节点
        GraphNode currentNode = getRandomNode();

        if (currentNode != null) {

            // 将当前节点添加到结果中
            walkResult.append(currentNode.word).append(" ");
            // 随机选择下一个节点的出边
            GraphEdge nextEdge = getRandomOutgoingEdge(currentNode);

            // 开始随机游走，如果没有下一个边，则停止游走
            while (nextEdge != null && !stopRequested) {
                

                // 如果已经经过的边中包含了当前边，则停止游走
                if (visitedEdges.contains(nextEdge)) {
                    walkResult.append(nextEdge.target.word).append(" ");
                    break;
                }

                // 将当前边添加到已经经过的边集合中
                visitedEdges.add(nextEdge);

                // 移动到下一个节点
                currentNode = nextEdge.target;

                // 将当前节点添加到结果中
                walkResult.append(currentNode.word).append(" ");

                // 随机选择下一个节点的出边
                nextEdge = getRandomOutgoingEdge(currentNode);
                try {
                    Thread.sleep(1000);  // Pause for 1 second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Restore interrupted status
                    logger.log(Level.WARNING, "Random walk interrupted during sleep", e);
                    break;
                }
            }
        }

        // 将结果转换为字符串形式，并去除末尾的空格
        String result = walkResult.toString().trim();

        // 将结果写入文件
        writeWalkResultToFile(result);

        // 返回随机游走结果
        return "Random walk result: " + result;

    }

    public static List<String> readAndProcessText(String filePath) {
        List<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("[^a-zA-Z]", " ").toLowerCase();
                String[] wordArray = line.split("\\s+");
                for (String word : wordArray) {
                    if (!word.isEmpty()) {
                        words.add(word);
                    }
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading the file: {0}", e.getMessage());
        }

        return words;
    }

    public static DirectedGraph buildGraph(List<String> words) {
        DirectedGraph graph = new DirectedGraph();

        for (String word : words) {
            graph.addNode(word);
        }

        for (int i = 0; i < words.size() - 1; i++) {
            String from = words.get(i);
            String to = words.get(i + 1);
            graph.addEdge(from, to);
        }

        return graph;
    }

    private static Map<GraphNode, Integer> initDistanceMap() {
        Map<GraphNode, Integer> distance = new HashMap<>();
        for (String node : graph.getAllNodes()) {
            distance.put(graph.getNode(node), Integer.MAX_VALUE);
        }
        return distance;
    }

    private static Map<GraphNode, GraphNode> initPredecessorMap() {
        Map<GraphNode, GraphNode> predecessor = new HashMap<>();
        for (String node : graph.getAllNodes()) {
            predecessor.put(graph.getNode(node), null);
        }
        return predecessor;
    }

    private static void dijkstraAlgorithm(GraphNode endNode,
            Map<GraphNode, Integer> distance,
            Map<GraphNode, GraphNode> predecessor,
            PriorityQueue<GraphNode> queue) {
        while (!queue.isEmpty()) {
            GraphNode current = queue.poll();
            if (current == endNode) {
                break;
            }
            updateDistanceAndPredecessor(current, distance, predecessor, queue);
        }
    }

    private static void updateDistanceAndPredecessor(GraphNode current,
            Map<GraphNode, Integer> distance,
            Map<GraphNode, GraphNode> predecessor,
            PriorityQueue<GraphNode> queue) {
        for (GraphEdge edge : current.edges) {
            int dist = distance.get(current) + edge.weight;
            if (dist < distance.get(edge.target)) {
                distance.put(edge.target, dist);
                predecessor.put(edge.target, current);
                queue.remove(edge.target);
                queue.add(edge.target);
            }
        }
    }

    private static List<String> buildShortestPath(GraphNode endNode,
            Map<GraphNode, GraphNode> predecessor) {
        List<String> shortestPath = new ArrayList<>();
        GraphNode current = endNode;
        while (current != null) {
            shortestPath.add(current.word);
            current = predecessor.get(current);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }

    private static String formatShortestPath(List<String> shortestPath) {
        return String.join(" → ", shortestPath);
    }

    private static GraphNode getRandomNode() {
        List<String> nodeKeys = new ArrayList<>(graph.nodes.keySet());
        return graph.nodes.get(nodeKeys.get(random.nextInt(nodeKeys.size())));
    }

    private static GraphEdge getRandomOutgoingEdge(GraphNode node) {
        if (node.edges.isEmpty()) {
            return null;
        }
        return node.edges.get(random.nextInt(node.edges.size()));
    }

    private static void writeWalkResultToFile(String walkResult) {
        try (PrintWriter writer = new PrintWriter("random_walk_result.txt")) {
            writer.println(walkResult);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class GraphNode {
        String word;
        List<GraphEdge> edges;

        GraphNode(String word) {
            this.word = word;
            this.edges = new ArrayList<>();
        }

        void addEdge(GraphEdge edge) {
            edges.add(edge);
        }
    }

    static class GraphEdge {
        GraphNode target;
        int weight;

        GraphEdge(GraphNode target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

<<<<<<< HEAD
    static class DirectedGraph {
=======
    public static class DirectedGraph {
>>>>>>> Lab3b
        Map<String, GraphNode> nodes;

        DirectedGraph() {
            this.nodes = new HashMap<>();
        }

        void addNode(String word) {
            nodes.putIfAbsent(word, new GraphNode(word));
        }

        void addEdge(String from, String to) {
            GraphNode fromNode = nodes.get(from);
            GraphNode toNode = nodes.get(to);
            if (fromNode != null && toNode != null) {
                for (GraphEdge edge : fromNode.edges) {
                    if (edge.target == toNode) {
                        edge.weight++;
                        return;
                    }
                }
                fromNode.addEdge(new GraphEdge(toNode, 1));
            }
        }

        // 获取节点
        GraphNode getNode(String word) {
            return nodes.get(word);
        }

        // 获取图中所有节点
        Set<String> getAllNodes() {
            return nodes.keySet();
        }

        // 获取节点之间的边的权重
        int getEdgeWeight(String from, String to) {
            GraphNode fromNode = nodes.get(from);
            if (fromNode != null) {
                for (GraphEdge edge : fromNode.edges) {
                    if (edge.target.word.equals(to)) {
                        return edge.weight;
                    }
                }
            }
            return Integer.MAX_VALUE; // 返回一个足够大的值表示无穷大权重
        }
    }

    static class MyEdge {
        private final int weight;

        public MyEdge(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return String.valueOf(weight);
        }
    }
}
