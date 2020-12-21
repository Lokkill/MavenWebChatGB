package org.example;

public class Task4 {
    private final Object block = new Object();
    private volatile char currentWord = 'A';
    private volatile char previousWord;

    public static void main(String[] args) {
        final Task4 obj = new Task4();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                obj.printA();
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                obj.printB();
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                obj.printC();
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }

    private void printA(){
        synchronized (block){
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentWord != 'A' && previousWord != 'C'){
                        block.wait();
                    }
                    System.out.print("A");
                    currentWord = 'B';
                    previousWord = 'A';
                    block.notify();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void printB(){
        synchronized (block){
            try {
                for (int i = 0; i < 5; i++){
                    while (currentWord != 'B' && previousWord != 'A'){
                        block.wait();
                    }
                    System.out.print('B');
                    currentWord = 'C';
                    previousWord = 'B';
                    block.notify();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void printC(){
        synchronized (block){
            try {
                for (int i = 0; i < 5; i++){
                    while (currentWord != 'C' && previousWord != 'B'){
                        block.wait();
                    }
                    System.out.print('C');
                    currentWord = 'A';
                    previousWord = 'C';
                    block.notifyAll();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
