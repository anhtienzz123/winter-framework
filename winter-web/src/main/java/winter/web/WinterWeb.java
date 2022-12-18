package winter.web;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinterWeb {

    private List<Object> controllers;

    public void run() {
        try {
            TomcatServer tomcatServer = new TomcatServer();
            tomcatServer.start(controllers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
