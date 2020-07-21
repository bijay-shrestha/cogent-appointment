package com.cogent.cogentappointment.esewa.syncronization;

/**
 * @author smriti on 20/07/20
 */
class ThreadOne extends Thread {

    private final NonSynchronizedMethod nonSynchronizedMethod;

    ThreadOne(NonSynchronizedMethod nonSynchronizedMethod) {
        this.nonSynchronizedMethod = nonSynchronizedMethod;
    }

    @Override
    public void run() {
        nonSynchronizedMethod.printNumbers();
    }
}

class ThreadTwo extends Thread {

    private final NonSynchronizedMethod nonSynchronizedMethod;

    ThreadTwo(NonSynchronizedMethod nonSynchronizedMethod) {
        this.nonSynchronizedMethod = nonSynchronizedMethod;
    }

    @Override
    public void run() {
        nonSynchronizedMethod.printNumbers();
    }
}
