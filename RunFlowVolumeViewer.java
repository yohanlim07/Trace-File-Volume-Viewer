import javax.swing.SwingUtilities;
import java.awt.*;

public class RunFlowVolumeViewer implements Runnable {
    public void run() {
        FlowVolumeViewer c = new FlowVolumeViewer();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunFlowVolumeViewer());
    }

}