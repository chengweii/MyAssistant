package weihua.myassistant.service.me;

import weihua.myassistant.service.me.entity.BodyState;
import weihua.myassistant.service.me.entity.MindState;
import weihua.myassistant.service.me.entity.OuterSign;
import weihua.myassistant.service.me.entity.Reaction;
import weihua.myassistant.service.me.parts.Body;
import weihua.myassistant.service.me.parts.Mind;

public class Me {
	private Body body;
	private Mind mind;

	public Me(BodyState bodyState, MindState mindState) {
		body = new Body(bodyState);
		mind = new Mind(body, mindState);
	}

	public Reaction getResponse(OuterSign outerSign) {
		Reaction reaction = new Reaction();
		mind.Handle(outerSign, reaction);
		return reaction;
	}
}
