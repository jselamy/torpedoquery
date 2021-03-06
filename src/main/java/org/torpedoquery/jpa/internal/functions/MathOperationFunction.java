/**
 *
 *   Copyright 2011 Xavier Jodoin xjodoin@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package org.torpedoquery.jpa.internal.functions;

import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.internal.Parameter;
import org.torpedoquery.jpa.internal.Proxy;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.TorpedoMagic;
import org.torpedoquery.jpa.internal.handlers.ParameterQueryHandler;
import org.torpedoquery.jpa.internal.query.SelectorParameter;

public class MathOperationFunction<T> implements ComparableFunction<T> {

	private final Selector<T> leftOperand;
	private final String operator;
	private final Selector<T> rightOperand;
	private final Proxy proxy;

	public MathOperationFunction(Proxy proxy, Selector<T> leftOperand, String operator, Selector<T> rightOperand) {
		this.proxy = proxy;
		this.leftOperand = leftOperand;
		this.operator = operator;
		this.rightOperand = rightOperand;
	}

	@Override
	public String createQueryFragment(AtomicInteger incrementor) {
		return leftOperand.createQueryFragment(incrementor) + " " + operator + " " + rightOperand.createQueryFragment(incrementor);
	}

	@Override
	public Parameter<T> generateParameter(T value) {
		return TorpedoMagic.getTorpedoMethodHandler().handle(new ParameterQueryHandler<T>("function",value));
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

}
