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
package org.torpedoquery.jpa.internal;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.torpedoquery.jpa.internal.query.ValueParameter;

public interface Join {

	void appendWhereClause(StringBuilder builder, AtomicInteger incrementor);

	String getJoin(String alias, AtomicInteger incrementor);

	List<ValueParameter> getParams();

	void appendOrderBy(StringBuilder builder, AtomicInteger incrementor);

	void appendGroupBy(StringBuilder builder, AtomicInteger incrementor);

}
