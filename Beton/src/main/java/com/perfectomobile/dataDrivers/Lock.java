package com.perfectomobile.dataDrivers;

public class Lock{
	private boolean isLocked;
	
	
	public Lock() {
		this.isLocked = false;
	}

	public synchronized void lock() throws Exception{
		int waitCycles = 3;
		int waited = 0;
		while(isLocked){
			wait(5000);
			if(isLocked){
				waited++;
				System.out.println("File is locked, waiting...");
			}
			if(waited == waitCycles){
				throw new Exception("Lock wait time exceeded");
			}
		}
		isLocked = true;
	}

	public synchronized void unlock(){
		isLocked = false;
		notify();
	}
}
