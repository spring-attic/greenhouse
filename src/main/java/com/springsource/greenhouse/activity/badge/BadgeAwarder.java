/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springsource.greenhouse.activity.badge;

import com.springsource.greenhouse.activity.action.Action;

/**
 * Determines if a badge should be awarded when an Action is performed.
 * @author Keith Donald
 */
public interface BadgeAwarder<A extends Action> {
	
	/**
	 * Award a badge for the Action performed.
	 * Returns <code>null</code> if no badge is to be awarded for the Action.
	 */
	public AwardedBadge awardBadgeForAction(A action);
	
}
