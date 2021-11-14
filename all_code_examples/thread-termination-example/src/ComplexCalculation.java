import java.math.BigInteger;

public class ComplexCalculation {

    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        BigInteger result;

        PowerCalculatingThread thread1= new PowerCalculatingThread(base1,power1);
        PowerCalculatingThread thread2= new PowerCalculatingThread(base2,power2);

        thread1.setDaemon(true);
        thread2.setDaemon(true);
        thread1.start();
        thread2.start();

        thread1.join(1000);

        thread2.join(1000);

        result= thread1.getResult().add(thread2.getResult());
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {


            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i.add(BigInteger.ONE)) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("thread: "+Thread.currentThread().getName()+" interrupted");

                }
                result = result.multiply(base);
            }



           /*
           Implement the calculation of result = base ^ power
           */
        }

        public BigInteger getResult() {

            return result;
        }
    }


    public static void main(String [] args){

        ComplexCalculation calc= new ComplexCalculation();
        try {
            BigInteger result=calc.calculateResult(BigInteger.valueOf(200L),BigInteger.valueOf(100L), BigInteger.valueOf(20L),BigInteger.valueOf(100L));
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
