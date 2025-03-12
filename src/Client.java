import java.util.Scanner;

import static java.lang.System.exit;

public class Client {
    /**
     * Функция, запускающая цикл, принимающий запросы пользователя.
     *
     * я наклала в том, что пока играет музыка я могу нажимать Next, Prev и меняется значение lastid и тогда по Pause хотят прекратить не тот поток.
     * Вывод: при нажатии Next/Prev обязательно делать Pause.
     *
     * Мы должны понять, какой из индексев - next_ind, а какой cur_ind, чтобы паузить именно cur_ind
     */
    public void run() {
        Playlist p = new Playlist();
        Scanner sc = new Scanner(System.in);
        int next_ind = 0;
        //int cur_ind = 0;

        while (sc.hasNext()) {
            switch (sc.nextLine()) {
                case "AddSong", "Add", "a" -> {
                    System.out.println("Input path:");
                    p.add(sc.nextLine());
                }
                case "Play" -> {
                    try {
                        p.Play();
                    }
                    catch (IllegalThreadStateException e) {
                        System.err.println("Play: Illegal "+e.getMessage());
                    }
                }
                case "Pause" -> {
                    p.pause();
                }
                case "Next" -> {
                    p.pause();
                    if (next_ind == p.size() - 1) {
                        next_ind = 0;
                    } else {
                        next_ind += 1;
                    }
                    p.setPlayind(next_ind);
                    //p.play(lastind); // <- не использует потоки. тогда Next один раз работает, а дальше поток блокируется.
                    // проблема в том, что почему-то вылезает exception, когда хотим start опять вызвать. То ли
                    // открываем уже открытый поток то ли что...
                    //p.start();
                }
                case "Prev" -> {
                    p.pause();
                    if (next_ind == 0) {
                        next_ind = p.size() - 1;
                    } else {
                        next_ind -= 1;
                    }
                    p.setPlayind(next_ind);
                    System.out.println("Prev: Not ready yet...");
                }
                case "Ind" -> {
                    System.out.println("Index:"+next_ind+"\nSize:"+p.size());
                }
                case "exit", "Exit" -> {
                    exit(0);
                }
            }
        }

    }

}
