package org.kenyahmis.psmartlibrary.AcosCard;


/**
 * Created by GMwasi on 3/13/2018.
 */

public class SecurityOptionRegister {

	private boolean issuerCode_ = false;
    private boolean pin_ = false;
    private boolean accessCondition5_ = false;
    private boolean accessCondition4_ = false;
    private boolean accessCondition3_ = false;
    private boolean accessCondition2_ = false;
    private boolean accessCondition1_ = false;

    public SecurityOptionRegister()
    {
    	
    }
    
    public SecurityOptionRegister(byte securityOptionRegisterByte)
    {
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x80)
    		issuerCode_ = true;
    	    	
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x40)
    		pin_ = true;    	
    	
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x20)
    		accessCondition5_ = true;
    	    	
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x10)
    		accessCondition4_= true;    	
    	
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x08)
    		accessCondition3_ = true;    	
    	
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x04)
    		accessCondition2_ = true;
    	    	
    	if ((securityOptionRegisterByte & (byte)0x01) == (byte)0x02)
    		accessCondition1_ = true;
    }
    
    public boolean getIssuerCode() { return this.issuerCode_; }
    public void setIssuerCode(boolean issuerCode) { this.issuerCode_ = issuerCode; }
    
	public boolean getPin() { return this.pin_; }
	public void setPin(boolean pin) { this.pin_ = pin; }
	
	public boolean getAccessCondition5() { return this.accessCondition5_; }
	public void setAccessCondition5(boolean accessCondition5)
					{ this.accessCondition5_ = accessCondition5; }
	
	public boolean getAccessCondition4() { return this.accessCondition4_; }
	public void setAccessCondition4(boolean accessCondition4)
					{ this.accessCondition4_ = accessCondition4; }
	
	public boolean getAccessCondition3() { return this.accessCondition3_; }
	public void setAccessCondition3(boolean accessCondition3)
					{ this.accessCondition3_ = accessCondition3; }
	
	public boolean getAccessCondition2() { return this.accessCondition2_; }
	public void setAccessCondition2(boolean accessCondition2)
					{ this.accessCondition2_ = accessCondition2; }
	
	public boolean getAccessCondition1() { return this.accessCondition1_; }
	public void setAccessCondition1(boolean accessCondition1)
					{ this.accessCondition1_ = accessCondition1; }

	public byte getRawValue()
	{
		byte rawValue = 0x00;
		
		if (accessCondition1_)
			rawValue |= 0x02;
		
		if (accessCondition2_)
			rawValue |= 0x04;
		
		if (accessCondition3_)
			rawValue |= 0x08;
		
		if (accessCondition4_)
			rawValue |= 0x10;
		
		if (accessCondition5_)
			rawValue |= 0x20;
		
		if (pin_)
			rawValue |= 0x40;
		
		if (issuerCode_)
			rawValue |= 0x80;
		
		return rawValue;
		
	}    
}
