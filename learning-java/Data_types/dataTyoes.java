public class dataTyoes {
   /* to use any java data types you first declear the data types then the name of the variables
    * and the value of the variable
    */
    public static void main(String[] args) {
        //primitives data types

       /* byte. this can hold whole numbers between -128 to 127 */
       //side = 1 bit

        byte byt = 45;
        

        /* short. short can again hold whole numbers from -32768 to 32767 */
        //side = 2 byte

        short sht = 12345;

        /* int. known as intergals. the can only hold whole numbers but can hold large amounts
         * of them unlick byte and short.
         */
            //side = 4 byte

         int myInt = 123456787;


         /* long. as the name implies, long can hold really large amount of numbers, i mean it hold 
          * the largest amount of numbers among this for data types that holds whole numbers. 
          so this is no friquently used unless you want to work with very very large numbers
          */
            //side = 8 byte

            long myLong = 1234567333;  
            
            /* when working with a long do put the letter l at the end of the numbers so the
            computer will know it is long
             */

             long mylongL = 123243563733883837l;

             //float. this is use to hold about 6 decimal numbers
            //make sure to put the letter f at the end of the decimal when working with float
             //side = 4 byte

             float flt = 2.56781f;
             

             /* double. this can hold decimal number like float but unlick float this 
              * can hold up to 15 decimal numbers. and bu default when working with decimal the
              computer will assume you are working with double unless you put the letter f at the
              end of the decimal
              */
                 //side = 8 byte

              double dbl = 23.66777677;


              //boolean. this simply just true or false statement.
              //side = 1 bit

                boolean booleanMoreThan10 = (myInt > 10);
                boolean booleanLessThan10 = (myInt < 10);

              /* char. all that char hold is a single character and the chararcter is usaully 
              put in a single qoute. if you try to put it in a double qoute you will get an error
              bcus anything in a double qoute is a string */ 
               //side = 2 byte
              char myChar = 'J';



              /* nonprimitives data types */

              /* string is the nonprimitives data types in java. unlick char is can hold multiple
               * character and the is normally declear in doubles qoutes
               */
                 //side = varies
               String  myStr = "my name is justice";


                System.out.println(byt);
                System.out.println(sht);
                System.out.println(myChar);
                System.out.println(myInt);
                System.out.println(mylongL);
                System.out.println(myLong);
                System.out.println(dbl);
                System.out.println(myStr);
                System.out.println(flt);
                

    }
}
