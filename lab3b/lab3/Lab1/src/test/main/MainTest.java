package main;

import main.Main;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static main.Main.buildGraph;
import static main.Main.readAndProcessText;
import static org.junit.Assert.assertEquals;

public class MainTest {

    private final InputStream originalSystemIn = System.in;
    private ByteArrayInputStream mockInput;

    @Before
    public void setUp() {
        // 创建模拟输入流，包含预设的用户输入
        String input = "2\nto\nhappy\n"; // 模拟用户输入选项2，word1，word2
        mockInput = new ByteArrayInputStream(input.getBytes());
        System.setIn(mockInput); // 将 System.in 重定向到这个流
    }

    @Test
    public void testQueryBridgeWords() {
        // 准备测试数据和环境
        String filePath = "test/test2.txt";
        List<String> words = readAndProcessText(filePath);
        Main.DirectedGraph graph = buildGraph(words);

        try {
            System.setIn(mockInput);
            Main.main(null);
        } catch (InterruptedException e) {
            e.printStackTrace(); // 或者处理具体的异常情况
        }


        // 验证输出是否包含预期的内容
        String expectedOutput = "The bridge words from to to happy are: be"; // 期望输出中的部分文本
    }

    // 捕获控制台输出的方法
    private String getOutput() {
        InputStream savedSystemIn = System.in;
        Scanner scanner = new Scanner(System.in);
        StringBuilder outputBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            outputBuilder.append(scanner.nextLine()).append("\n");
        }
        System.setIn(savedSystemIn); // 恢复原始的 System.in
        return outputBuilder.toString();
    }

    @After
    public void tearDown() {
        // 恢复原始的 System.in
        System.setIn(originalSystemIn);
    }
}
