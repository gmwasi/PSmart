package org.kenyahmis.psmartlibrary.Models;

/**
 * Created by GMwasi on 3/1/2018.
 */

public class Apdu
{
    private byte _bCla;
    private byte _bIns;
    private byte _bP1;
    private byte _bP2;
    private byte _bP3;
    private int _LengthExpected;

    private byte[] _SendData;
    private byte[] _ReceiveData;
    private byte[] _Sw;

    public Apdu()
    {
        _LengthExpected = 0;
    }

    /// <summary>
    /// The T=0 instruction class.
    /// </summary>
    public byte getCla() {return this._bCla;}
    public void setCla(byte bCla) {this._bCla = bCla;}

    /// <summary>
    /// An instruction code in the T=0 instruction class.
    /// </summary>
    public byte getIns() {return this._bIns;}
    public void setIns(byte bIns){this._bIns = bIns;}

    /// <summary>
    /// Reference codes that complete the instruction code.
    /// </summary>
    public byte getP1() {return this._bP1;}
    public void setP1(byte bP1) {this._bP1 = bP1;}

    /// <summary>
    /// Reference codes that complete the instruction code.
    /// </summary>
    public byte getP2() {return this._bP2;}
    public void setP2(byte bP2) {this._bP2 = bP2;}

    /// <summary>
    /// The number of data bytes to be transmitted during the command, per ISO 7816-4, Section 8.2.1.
    /// </summary>
    public byte getP3() {return this._bP3;}
    public void setP3(byte bP3) {this._bP3 = bP3;}

    /// <summary>
    /// Length of data expected from the card
    /// </summary>
    public int getLengthExpected() {return this._LengthExpected;}
    public void setLengthExpected(int LengthExpected) {this._LengthExpected = LengthExpected;}

    public byte[] getSendData(){return this._SendData;}
    public void setSendData(byte[] sendData) {this._SendData = sendData;}

    public byte[] getReceiveData(){return this._ReceiveData;}
    public void setReceiveData(byte[] receiveData) {this._ReceiveData = receiveData;}

    public byte[] getSw() {return this._Sw;}
    public void setSw(byte[] sw) {this._Sw = sw;}

    public void setCommand(byte[] cmd) throws Exception
    {
        if(cmd.length != 5)
            throw new Exception("Invalid command");

        setCla(cmd[0]);
        setIns(cmd[1]);
        setP1(cmd[2]);
        setP2(cmd[3]);
        setP3(cmd[4]);

        return;
    }

    public boolean swEqualTo(byte[] data)
    {
        if(getSw() == null)
            return false;

        for(int i = 0; i < getSw().length; i++)
            if(getSw()[i] != data[i])
                return false;

        return true;
    }
}
