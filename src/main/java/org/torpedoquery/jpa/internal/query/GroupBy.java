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
package org.torpedoquery.jpa.internal.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.ComparableFunction;
import org.torpedoquery.jpa.Function;
import org.torpedoquery.jpa.OnGoingCollectionCondition;
import org.torpedoquery.jpa.OnGoingComparableCondition;
import org.torpedoquery.jpa.OnGoingCondition;
import org.torpedoquery.jpa.OnGoingGroupByCondition;
import org.torpedoquery.jpa.OnGoingLogicalCondition;
import org.torpedoquery.jpa.OnGoingStringCondition;
import org.torpedoquery.jpa.ValueOnGoingCondition;
import org.torpedoquery.jpa.internal.Condition;
import org.torpedoquery.jpa.internal.Selector;
import org.torpedoquery.jpa.internal.conditions.ConditionBuilder;
import org.torpedoquery.jpa.internal.conditions.ConditionHelper;
import org.torpedoquery.jpa.internal.conditions.GroupingCondition;
import org.torpedoquery.jpa.internal.conditions.LogicalCondition;

public class GroupBy implements OnGoingGroupByCondition {

	private final List<Selector> groups = new ArrayList<Selector>();
	private ConditionBuilder havingCondition;

	public String createQueryFragment(StringBuilder builder, AtomicInteger incrementor) {

		if (!groups.isEmpty()) {
			Iterator<Selector> iterator = groups.iterator();

			if (builder.length() == 0) {
				builder.append(" group by ").append(iterator.next().createQueryFragment(incrementor));
			}

			while (iterator.hasNext()) {
				Selector selector = iterator.next();
				builder.append(",").append(selector.createQueryFragment(incrementor));
			}

			if (havingCondition != null) {
				builder.append(" having ").append(ConditionHelper.getConditionClause(havingCondition).createQueryFragment(incrementor));
			}

			return builder.toString();
		}
		return "";
	}

	public void addGroup(Selector selector) {
		groups.add(selector);
	}

	@Override
	public <T> ValueOnGoingCondition<T> having(T object) {
		ValueOnGoingCondition<T> createCondition = ConditionHelper.<T, ValueOnGoingCondition<T>> createCondition(null);
		havingCondition = (ConditionBuilder) createCondition;
		return createCondition;
	}

	@Override
	public <V, T extends Comparable<V>> OnGoingComparableCondition<V> having(T object) {
		OnGoingComparableCondition<V> createCondition = ConditionHelper.<V, OnGoingComparableCondition<V>> createCondition(null);
		havingCondition = (ConditionBuilder) createCondition;
		return createCondition;
	}

	@Override
	public OnGoingStringCondition<String> having(String object) {
		OnGoingStringCondition<String> createCondition = ConditionHelper.<String, OnGoingStringCondition<String>> createCondition(null);
		havingCondition = (ConditionBuilder) createCondition;
		return createCondition;
	}

	@Override
	public <T> OnGoingCollectionCondition<T> having(Collection<T> object) {
		OnGoingCollectionCondition<T> createCollectionCondition = ConditionHelper.<T, OnGoingCollectionCondition<T>> createCondition(null);
		havingCondition = (ConditionBuilder) createCollectionCondition;
		return createCollectionCondition;
	}

	@Override
	public <T> ValueOnGoingCondition<T> having(Function<T> function) {
		ValueOnGoingCondition<T> createCondition = ConditionHelper.<T, ValueOnGoingCondition<T>> createCondition(function, null);
		havingCondition = (ConditionBuilder) createCondition;
		return createCondition;
	}

	@Override
	public <T extends Comparable<?>> OnGoingComparableCondition<T> having(ComparableFunction<T> function) {
		OnGoingComparableCondition<T> createCondition = ConditionHelper.<T, OnGoingComparableCondition<T>> createCondition(function, null);
		havingCondition = (ConditionBuilder) createCondition;
		return createCondition;
	}
	
	@Override
	public OnGoingLogicalCondition having(OnGoingLogicalCondition condition) {
		LogicalCondition logicalCondition = (LogicalCondition)condition;
		QueryBuilder builder = (QueryBuilder) logicalCondition.getBuilder();
		LogicalCondition groupingLogicalCondition = new LogicalCondition(builder, new GroupingCondition(logicalCondition));
		havingCondition = new ConditionBuilder(builder, groupingLogicalCondition, null);
		return (OnGoingLogicalCondition) groupingLogicalCondition;
	}
	
	public Condition getCondition()
	{
		return ConditionHelper.getConditionClause(havingCondition);
	}
}
