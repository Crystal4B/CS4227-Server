package dev.platinum.hotel.types;

import java.sql.Timestamp;

public class Voucher {
    private int id;
	private String type;
	private double amount;
    private Timestamp issue_date;
	private Timestamp expiry_date ;
	private User creator  ;
	private Reservation available ;

    /**
	 * Empty voucher constructor
	 */
    public Voucher(){}

    /**
	 * Constructor using id
	 * @param num the id of the voucher in the database
	 */
    public Voucher(int num)
	{
		id = num;
	}

    /**
	 * Constructor using arrivalDate, departureDate and rooms
	 * @param iss_date the issue date of the voucher in the database
     * @param exp_date the expiry date of the voucher in the database
	 * @param temp the type of the voucher in the database
	 * @param num the amount of the voucher in the database
	 * @param user the creator of the voucher in the database
	 */
    public Voucher(Timestamp iss_date, Timestamp exp_date, String temp, double num, User user)
	{
        issue_date = iss_date;
        expiry_date = exp_date;
        type = temp;
        amount = num;
        creator = user;
	}

    /**
	 * Constructor using arrivalDate, departureDate and rooms
     * @param num the id of the voucher in the database
	 * @param iss_date the issue date of the voucher in the database
     * @param exp_date the expiry date of the voucher in the database
	 * @param temp the type of the voucher in the database
	 * @param num the amount of the voucher in the database
	 * @param user the creator of the voucher in the database
	 */
    public Voucher(int id, Timestamp iss_date, Timestamp exp_date, String temp, double num, User user)
	{
        this.id = id;
        issue_date = iss_date;
        expiry_date = exp_date;
        type = temp;
        amount = num;
        creator = user;
	}

    /**
	 * Constructor using arrivalDate, departureDate and rooms
     * @param num the id of the voucher in the database
	 * @param iss_date the issue date of the voucher in the database
     * @param exp_date the expiry date of the voucher in the database
	 * @param temp the type of the voucher in the database
	 * @param num the amount of the voucher in the database
	 * @param user the creator of the voucher in the database
     * @param availability the availability of the voucher in the database
	 */
    public Voucher(int id, Timestamp iss_date, Timestamp exp_date, String temp, double num, User user, Reservation availability)
	{
        this.id = id;
        issue_date = iss_date;
        expiry_date = exp_date;
        type = temp;
        amount = num;
        creator = user;
        available = availability;
	}
    
    /**
	 * Simple id getter
	 * @return id of voucher as int
	 */
    public int getID(){
        return id;
    }

    /**
	 * Simple type getter
	 * @return type of voucher as string
	 */
    public String getType(){
        return type;
    }

    /**
	 * Simple amount getter
	 * @return amount of voucher as double
	 */
    public double getAmount(){
        return amount;
    }

    /**
	 * Simple issue date getter
	 * @return issue date of voucher as timestamp
	 */
    public Timestamp getIssueDate(){
        return issue_date;
    }

    /**
	 * Simple expiry date getter
	 * @return expiry date of voucher as timestamp
	 */
    public Timestamp getExpiryDate(){
        return expiry_date;
    }
    
    /**
	 * Simple creator getter
	 * @return creator of voucher as user
	 */
    public User getCreator(){
        return creator;
    }

    /**
	 * Simple availability getter
	 * @return available of voucher as reservation
	 */
    public Reservation getAvailability(){
        return available;
    }

	/**
	 * Simple id setter
	 * @param num desired id for voucher
	 */
    public void setID(int num){
        id = num;
    }

    /**
	 * Simple type setter
	 * @param temp desired type for voucher
	 */
    public void setType(String temp){
        type = temp;
    }

    /**
	 * Simple amount setter
	 * @param num desired amount for voucher
	 */
    public void setAmount(double num){
        amount = num;
    }
    
    /**
	 * Simple issue date setter
	 * @param date desired issue date for voucher
	 */
    public void setIssueDate(Timestamp date){
        issue_date = date;
    }

    /**
	 * Simple expiry date setter
	 * @param date desired expiry date for voucher
	 */
    public void setExpiryDate(Timestamp date){
        expiry_date = date;
    }
    
    /**
	 * Simple user setter
	 * @param user desired user for voucher
	 */
    public void setCreator(User user){
        creator = user;
    }
    
    /**
	 * Simple availablity setter
	 * @param availability desired availability for voucher
	 */
    public void setAvailability(Reservation availability){
        available = availability;
    }

}
