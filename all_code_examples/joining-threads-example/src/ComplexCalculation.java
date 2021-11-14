import java.math.BigInteger;

public class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result;

        PowerCalculatingThread thread1= new PowerCalculatingThread(base1,power1);
        PowerCalculatingThread thread2= new PowerCalculatingThread(base2,power2);

        thread1.start();
        thread2.start();
        try{
            thread1.join(200);
            thread2.join(200);
        }catch(InterruptedException ie){
            result=BigInteger.ZERO;

        }
        result=thread1.getResult().add(thread2.getResult());
         
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        return result;
    }

    public static void main(String args[]){

        ComplexCalculation cc= new ComplexCalculation();
       System.out.println(cc.calculateResult(BigInteger.valueOf(2L),BigInteger.valueOf(2L),BigInteger.valueOf(3L),BigInteger.valueOf(3L)));
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

            try{
                BigInteger index=BigInteger.ONE;
                while(index.compareTo(power)<=0){


                    result=result.multiply(base);
                    index=index.add(BigInteger.ONE);
                }
            }catch(Exception e){
                result=BigInteger.ZERO;
            }

        }

        public BigInteger getResult() { return result; }
    }
}