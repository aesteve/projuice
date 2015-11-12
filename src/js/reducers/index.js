import { combineReducers } from 'redux';

import login from './login';
import projects from './projects';
import users from './users';
import myInfos from './my-infos';
import tokenStatus from './token-status';
import issues from './issues';

export default combineReducers({
	login,
	projects,
	users,
	myInfos,
	tokenStatus,
	issues
});
