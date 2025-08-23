import React from 'react';
import '../style/PrivacyPolicy.css';
import {Link} from "react-router-dom";

function PrivacyPolicy({onPrivacyPolicy}) {
    return (
        <div className="container privacy-container">
            <div className="card privacy-card">
                <h1 className="section-title">Privacy Policy</h1>

                <section className="policy-section">
                    <h2 className="subsection-title">Introduction</h2>
                    <p className="content-text">Welcome to Cherry. We respect your privacy and are committed to protecting your personal data. This Privacy Policy explains how we collect, use, and safeguard your information when you use our nutrition tracking app.</p>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">Information We Collect</h2>
                    <p className="content-text">We collect the following types of information:</p>
                    <ul className="policy-list">
                        <li><strong>Account Information:</strong> Email address, username, and authentication details</li>
                        <li><strong>Nutrition Data:</strong> Your weight, meals, food items, calories, protein, and other nutritional or fitness related information you enter</li>
                        <li><strong>Usage Data:</strong> How you interact with our app, features you use, and preferences</li>
                    </ul>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">How We Use Your Information</h2>
                    <ul className="policy-list">
                        <li>To provide, maintain, and improve our services</li>
                        <li>To calculate nutritional totals and provide insights</li>
                        <li>To personalize your experience and recommendations</li>
                        <li>To communicate with you about updates or changes</li>
                        <li>To ensure the security of our platform</li>
                    </ul>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">Data Storage and Security</h2>
                    <p className="content-text">We implement appropriate security measures to protect your personal information. Your data is stored securely and we use industry-standard protection methods.</p>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">Data Sharing</h2>
                    <p className="content-text">We do not sell your personal data. We may share data with:</p>
                    <ul className="policy-list">
                        <li>Service providers who help us operate our platform</li>
                        <li>Legal authorities when required by law</li>
                        <li>With your explicit consent for specific purposes</li>
                    </ul>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">Your Rights</h2>
                    <p className="content-text">You have the right to:</p>
                    <ul className="policy-list">
                        <li>Access your personal data</li>
                        <li>Correct inaccurate data</li>
                        <li>Delete your personal data</li>
                        <li>Export your data</li>
                        <li>Withdraw consent at any time</li>
                    </ul>
                    <p className="content-text">To exercise these rights, contact us through our in-app support or email us at <a href="mailto:cherry@joshroundy.dev" className="link">cherry@joshroundy.dev</a>.</p>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">Changes to This Policy</h2>
                    <p className="content-text">We may update this Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page.</p>
                </section>

                <section className="policy-section">
                    <h2 className="subsection-title">Contact Us</h2>
                    <p className="content-text">If you have questions about this Privacy Policy, please contact us at <a href="mailto:cherry@joshroundy.dev" className="link">cherry@joshroundy.dev</a>.</p>
                </section>

                <div className="button-container">
                    <Link to={'/'} className={"primary-button"}>Back to App</Link>
                </div>
            </div>
        </div>
    );
}

export default PrivacyPolicy;