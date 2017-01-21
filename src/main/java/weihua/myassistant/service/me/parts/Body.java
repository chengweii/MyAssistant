package weihua.myassistant.service.me.parts;

import weihua.myassistant.service.me.entity.BodyState;
import weihua.myassistant.service.me.entity.Reaction;

public class Body {
	public BodyState bodyState;

	public Body(BodyState bodyState) {
		this.bodyState = bodyState;
	}

	public void Handle(Reaction reaction) {
	}
}
