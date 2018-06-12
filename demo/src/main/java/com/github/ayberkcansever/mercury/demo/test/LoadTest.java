package com.github.ayberkcansever.mercury.demo.test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoadTest {

    public static void main(String[] args) throws InterruptedException, IOException {

        LoadClient loadClient = new LoadClient(1,"localhost", 5555);
        LoadClient loadClient2 = new LoadClient(2,"localhost", 5556);
        LoadClient loadClient3 = new LoadClient(3,"localhost", 5557);
        LoadClient loadClient4 = new LoadClient(4,"localhost", 5558);

        TimeUnit.SECONDS.sleep(3);

        loadClient.send("id:client1");
        loadClient2.send("id:client2");
        loadClient3.send("id:client3");
        loadClient4.send("id:client4");

        TimeUnit.SECONDS.sleep(2);

        new MessageThread(loadClient).start();
        new MessageThread(loadClient2).start();
        new MessageThread(loadClient3).start();
        new MessageThread(loadClient4).start();

    }

    static class MessageThread extends Thread {

        private LoadClient senderClient;

        public MessageThread(LoadClient senderClient) {
            this.senderClient = senderClient;
        }

        public void run() {
            for(int i = 1; i <= 4; i++) {
                if(senderClient.getId() == i) {
                    continue;
                }
                for(int j = 0; j < 3000; j++) {
                    senderClient.send("send:client" + i + ":msg" + j + "\0");
                }
            }
        }
    }

}
