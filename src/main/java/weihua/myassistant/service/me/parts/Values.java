package weihua.myassistant.service.me.parts;

import weihua.myassistant.service.me.entity.BodyState;
import weihua.myassistant.service.me.entity.MindState;
import weihua.myassistant.service.me.entity.OuterSign;
import weihua.myassistant.service.me.entity.Reaction;

public class Values {
	private BodyState bodyState;
	private MindState mindState;

	public Values(BodyState bodyState, MindState mindState) {
		this.mindState = mindState;
		this.mindState = mindState;
	}

	public void Handle(OuterSign outerSign, Reaction reaction) {
	}
}
