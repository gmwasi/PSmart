package org.kenyahmis.psmartlibrary.AcosCard;

/**
 * Created by GMwasi on 3/13/2018.
 */

public class OptionRegister {
	
	private boolean enableAccount_ = false;
    private boolean enableTripleDes_ = false;
    private boolean enableChangePinCommand_ = false;
    private boolean enableDebitMac_ = false;
    private boolean requirePinDuringDebit_ = false;
    private boolean enableRevokeDebitCommand_ = false;
    private boolean requireMutualAuthenticationOnAccountTransaction_ = false;
    private boolean requireMutualAuthenticationOnInquireAccount_ = false;
    
    public OptionRegister()
    {
    	
    }
    
    public OptionRegister (byte optionRegisterByte)
    {
    	if ((optionRegisterByte & (byte)0x01) == 0x01)
    		enableAccount_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x02)
    		enableTripleDes_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x04)
    		enableChangePinCommand_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x08)
    		enableDebitMac_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x10)
    		requirePinDuringDebit_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x20)
    		enableRevokeDebitCommand_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x40)
    		requireMutualAuthenticationOnAccountTransaction_ = true;
    	
    	if ((optionRegisterByte & (byte)0x01) == 0x80)
    		requireMutualAuthenticationOnInquireAccount_ = true;    		
    }

    public boolean getEnableAccount() { return this.enableAccount_; }
    public void setEnableAccount(boolean enableAccount) { this.enableAccount_ = enableAccount; }
    
    public boolean getEnableTripleDes() { return this.enableTripleDes_; }
    public void setEnableTripleDes(boolean enableTripleDes) { this.enableTripleDes_ = enableTripleDes; }
    
    public boolean getEnableChangePinCommand()
    							{ return this.enableChangePinCommand_; }
    public void setEnableChangePinCommand(boolean enableChangePinCommand)
    							{ this.enableChangePinCommand_ = enableChangePinCommand; }
    
    public boolean getEnableDebitMac() { return this.enableDebitMac_; }
    public void setEnableDebitMac(boolean enableDebitMac)
    							{ this.enableDebitMac_ = enableDebitMac; }
    
    public boolean getRequirePinDuringDebit()
    							{ return this.requirePinDuringDebit_; }
    public void setRequirePinDuringDebit(boolean requirePinDuringDebit)
    							{ this.requirePinDuringDebit_ = requirePinDuringDebit; }
    
    public boolean getEnableRevokeDebitCommand()
    							{ return this.enableRevokeDebitCommand_; }
    public void setEnableRevokeDebitCommand(boolean enableRevokeDebitCommand)
    							{ this.enableRevokeDebitCommand_ = enableRevokeDebitCommand; }
    
    public boolean getRequireMutualAuthenticationOnAccountTransaction()
    							{ return this.requireMutualAuthenticationOnAccountTransaction_; }
    public void setRequireMutualAuthenticationOnAccountTransaction(boolean requireMutualAuthenticationOnAccountTransaction)
    							{ this.requireMutualAuthenticationOnAccountTransaction_ = requireMutualAuthenticationOnAccountTransaction; }
    
    public boolean getRequireMutualAuthenticationOnInquireAccount()
    							{ return this.requireMutualAuthenticationOnInquireAccount_; }
    public void setRequireMutualAuthenticationOnInquireAccount(boolean requireMutualAuthenticationOnInquireAccount)
    							{ this.requireMutualAuthenticationOnInquireAccount_ = requireMutualAuthenticationOnInquireAccount; }

    public byte getRawValue()
    {
    	byte rawValue = 0x00;
    	
        if (enableAccount_)
            rawValue |= 0x01;

        if (enableTripleDes_)
            rawValue |= 0x02;

        if (enableChangePinCommand_)
            rawValue |= 0x04;

        if (enableDebitMac_)
            rawValue |= 0x08;

        if (requirePinDuringDebit_)
            rawValue |= 0x10;

        if (enableRevokeDebitCommand_)
            rawValue |= 0x20;

        if (requireMutualAuthenticationOnAccountTransaction_)
            rawValue |= 0x40;

        if (requireMutualAuthenticationOnInquireAccount_)
            rawValue |= 0x80;

        return rawValue;
    	
    }


}
