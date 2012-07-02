package Design_tool_prototype1;

import javax.swing.JFrame;
import javax.swing.JButton;

public class frame_test {

    frame_test(){
        char[] data = {'i','n','f','o','r','m','a','t','i','o','n'};
        System.out.println(String.valueOf(data, 2, 7));
        JFrame my_frame = new JFrame();
        my_frame.setBounds(0, 0, 800, 400);
        my_frame.setVisible(true);
        JButton my_button = new JButton("test button");
        my_frame.add(my_button);
        my_button.setBounds(20, 20, 100, 100);
    }
}
