
export function LoadingState () {
    return (
    <div className="Loading-container">
        <div className="Spinner"></div>
    </div>
    );
}

export function ErrorState ({ message }) {
    return (
        <div className="Error-container">
            <p>{message}</p>
        </div>
    );
}