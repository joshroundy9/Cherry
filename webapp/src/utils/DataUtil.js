export const genericDataRequest = async (url, method, headers, body) => {
    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 8000); // 8 seconds timeout
    try {
        const res = await fetch(url, {
            method: method,
            headers: headers,
            body: body,
            signal: controller.signal
        });
        if (!res.ok) throw new Error("Error making request: " + res.status);

        const text = await res.text();
        if (!text) return null; // No content to parse
        return JSON.parse(text);
    } catch (err) {
        alert(`Request failed: ${err.message}`);
        throw err;
    } finally {
        clearTimeout(timeoutId);
    }
};