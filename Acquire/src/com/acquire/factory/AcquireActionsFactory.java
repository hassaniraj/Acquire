package com.acquire.factory;

import com.acquire.actions.AcquireActions;
import com.acquire.actions.IAcquireActions;

public class AcquireActionsFactory {

	public static AcquireActions getInstance() {
		return new IAcquireActions();
	}
}
