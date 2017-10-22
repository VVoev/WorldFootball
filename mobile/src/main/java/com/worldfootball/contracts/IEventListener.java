package com.worldfootball.contracts;

import com.worldfootball.models.EventData;

public interface IEventListener {
	void onEvent(EventData event);
}
