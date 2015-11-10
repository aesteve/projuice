export function injectResponseAsState(component, resultNode) {
	return (err, res) => {
		const state = {};
		if (err) {
			state.status = 'error';
			state.error = err;
		} else {
			state.error = null;
			state.status = 'done';
			state[resultNode] = res.body;
		}
		component.setState(state);
	}
}
